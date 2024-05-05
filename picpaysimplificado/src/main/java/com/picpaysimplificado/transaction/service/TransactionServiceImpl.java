package com.picpaysimplificado.transaction.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.authorization.service.AuthorizationService;
import com.picpaysimplificado.notification.service.NotificationService;
import com.picpaysimplificado.transaction.entity.TransactionEntity;
import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.transaction.repository.TransactionRepository;
import com.picpaysimplificado.user.entity.UserEntity;
import com.picpaysimplificado.user.repository.UserRepository;
import com.picpaysimplificado.utils.exception.EntityException;
import com.picpaysimplificado.utils.exception.NotFoundException;
import com.picpaysimplificado.utils.exception.PicPayException;
import com.picpaysimplificado.wallet.enums.WalletType;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void transfer(TransactionRequest request) throws PicPayException {
        // Validar regras de negócio
        this.validate(request);

        // Autorizar transaction pela API
        this.authorizationService.authorizeTransaction(request);

        // Criar transação
        final UserEntity payee = this.userRepository.findById(request.payee()).get();
        final UserEntity payer = this.userRepository.findById(request.payer()).get();

        this.saveTransactionEntity(request, payer, payee);
        
        // Remover valor do peyer
        this.debitWallet(payer, request.value());

        // Adicionar valor no peyee
        this.creditWallet(payee, request.value());

        // Notificar transação
        this.notificationService.notifyTransaction(request);
    }

    private void creditWallet(UserEntity payee, BigDecimal value) {
        payee.getWallet().setBalence(payee.getWallet().getBalence().add(value));

        this.userRepository.save(payee);
    }

    private void debitWallet(UserEntity payer, BigDecimal value) {
        payer.getWallet().setBalence(payer.getWallet().getBalence().subtract(value));

        this.userRepository.save(payer);
    }

    private void saveTransactionEntity(TransactionRequest request, UserEntity payer, UserEntity payee) {
        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setPayer(payer);
        transactionEntity.setPayee(payee);
        transactionEntity.setValue(request.value());

        this.transactionRepository.save(transactionEntity);
    }

    private void validate(final TransactionRequest request) throws PicPayException {
        final UserEntity payee = this.userRepository.findById(request.payee()).orElseThrow(() -> new NotFoundException( "Usuário não encontrado " + request.payee() ));
        final UserEntity payer = this.userRepository.findById(request.payer()).orElseThrow(() -> new NotFoundException( "Usuário não encontrado " + request.payee() ));

        this.validateWalletType(payer);

        this.validateBalance(request, payer);

        this.validateSameUser(payee, payer);
    }
    
    private void validateWalletType(UserEntity payer) throws EntityException {
        if(!payer.getWallet().getType().equals(WalletType.COMMON)){
            throw new EntityException( "Tipo de carteira inválida" );
        }
    }
    
    private void validateBalance(TransactionRequest request, UserEntity payer) throws EntityException {
        if(payer.getWallet().getBalence().compareTo(request.value()) < 1){
            throw new EntityException( "Saldo insuficiente" );
        }
    }
    
    private void validateSameUser(UserEntity payee, UserEntity payer) throws EntityException {
        if(payer.getId().equals(payee.getId())){
            throw new EntityException( "Não é possível transferir valor para o mesmo usuário" );
        }
    }
}
