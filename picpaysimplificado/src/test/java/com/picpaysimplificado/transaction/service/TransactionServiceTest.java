package com.picpaysimplificado.transaction.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import com.picpaysimplificado.authorization.service.AuthorizationService;
import com.picpaysimplificado.notification.service.NotificationService;
import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.transaction.repository.TransactionRepository;
import com.picpaysimplificado.user.entity.UserEntity;
import com.picpaysimplificado.user.repository.UserRepository;
import com.picpaysimplificado.utils.exception.EntityException;
import com.picpaysimplificado.utils.exception.ForbiddenException;
import com.picpaysimplificado.utils.exception.PicPayException;
import com.picpaysimplificado.wallet.entity.WalletEntity;
import com.picpaysimplificado.wallet.enums.WalletType;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceTest {

    @Autowired
    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransactionRepository transactionRepository;

    private UserEntity userCommon;

    private UserEntity userMerchant;

    @BeforeEach
    void setup() throws PicPayException{
        UserEntity userCommon = new UserEntity();
        userCommon.setId(1L);
        WalletEntity walletCommon = new WalletEntity();
        walletCommon.setId(1L);
        walletCommon.setType(WalletType.COMMON);
        walletCommon.setUser(userCommon);
        walletCommon.setBalence(new BigDecimal(1000L));
        userCommon.setWallet(walletCommon);
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(userCommon));
        this.userCommon = userCommon;
        
        UserEntity userMerchant = new UserEntity();
        userMerchant.setId(2L);
        WalletEntity walletMerchant = new WalletEntity();
        walletMerchant.setId(2L);
        walletMerchant.setType(WalletType.MERCHANT);
        walletMerchant.setUser(userMerchant);
        walletMerchant.setBalence(new BigDecimal(500L));
        userMerchant.setWallet(walletMerchant);
        Mockito.when(this.userRepository.findById(2L)).thenReturn(Optional.of(userMerchant));
        this.userMerchant = userMerchant;
        
        TransactionRequest transaction = new TransactionRequest(new BigDecimal(100), 1L, 2L);
        Mockito.doThrow(new ForbiddenException("Erro")).when(this.authorizationService).authorizeTransaction(transaction);;
    }

    @Test
    void errorPayerWalletCommon() throws Exception {
        EntityException exception = Assertions.assertThrows(EntityException.class, () -> {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(1500), 2L, 1L);
            this.service.transfer(transaction);
        });

        Assertions.assertEquals("Tipo de carteira inválida", exception.getMessage());
    }

    @Test
    void errorPayerWithoutEnoughtBalance() throws Exception {
        EntityException exception = Assertions.assertThrows(EntityException.class, () -> {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(1500), 1L, 2L);
            this.service.transfer(transaction);
        });

        Assertions.assertEquals("Saldo insuficiente", exception.getMessage());
    }

    @Test
    void errorSameUser() throws Exception {
        EntityException exception = Assertions.assertThrows(EntityException.class, () -> {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(100), 1L, 1L);
            this.service.transfer(transaction);
        });

        Assertions.assertEquals("Não é possível transferir valor para o mesmo usuário", exception.getMessage());
    }

    @Test
    void errorAuthorizationForbidden() throws Exception {
        Assertions.assertThrows(ForbiddenException.class, () -> {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(100), 1L, 2L);
            this.service.transfer(transaction);
        });
    }

    @Test
    void transactionSuccess() throws Exception {
        TransactionRequest transaction = new TransactionRequest(new BigDecimal(500), 1L, 2L);
        this.service.transfer(transaction);

        BigDecimal balancePayer = new BigDecimal(500L);
        BigDecimal balancePayee = new BigDecimal(1000L);
        Assertions.assertTrue(this.userCommon.getWallet().getBalence().compareTo(balancePayer) == 0);
        Assertions.assertTrue(this.userMerchant.getWallet().getBalence().compareTo(balancePayee) == 0);
    }
}

