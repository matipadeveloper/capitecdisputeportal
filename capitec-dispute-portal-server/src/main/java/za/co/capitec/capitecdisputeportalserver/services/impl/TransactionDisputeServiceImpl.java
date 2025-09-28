package za.co.capitec.capitecdisputeportalserver.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.capitec.capitecdisputeportalserver.entities.TransactionDispute;
import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;
import za.co.capitec.capitecdisputeportalserver.requests.TransactionDisputeRequest;
import za.co.capitec.capitecdisputeportalserver.responses.TransactionDisputeResponse;
import za.co.capitec.capitecdisputeportalserver.repositories.DisputeRepository;
import za.co.capitec.capitecdisputeportalserver.repositories.PaymentTransactionRepository;
import za.co.capitec.capitecdisputeportalserver.services.TransactionDisputeService;
import za.co.capitec.capitecdisputeportalserver.utils.TransactionDisputeMapper;
import za.co.capitec.capitecdisputeportalserver.exception.TransactionNotFoundException;
import za.co.capitec.capitecdisputeportalserver.exception.DisputeNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionDisputeServiceImpl implements TransactionDisputeService {

    private final DisputeRepository disputeRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    public TransactionDisputeServiceImpl(DisputeRepository disputeRepository, PaymentTransactionRepository paymentTransactionRepository) {
        this.disputeRepository = disputeRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Override
    public List<TransactionDisputeResponse> getAllDisputes() {
        return disputeRepository.findAll()
                .stream()
                .map(TransactionDisputeMapper::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TransactionDisputeResponse> getDisputeById(Long id) {
        return disputeRepository.findById(id)
                .map(TransactionDisputeMapper::convertToResponse);
    }

    @Override
    public List<TransactionDisputeResponse> getDisputesByTransactionId(Long transactionId) {
        return disputeRepository.findByTransaction_Id(transactionId)
                .stream()
                .map(TransactionDisputeMapper::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDisputeResponse createDispute(TransactionDisputeRequest disputeRequest) {
        PaymentTransaction transaction = paymentTransactionRepository.findById(disputeRequest.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + disputeRequest.getTransactionId()));

        TransactionDispute dispute = TransactionDisputeMapper.convertToEntity(disputeRequest, transaction);
        TransactionDispute savedDispute = disputeRepository.save(dispute);
        return TransactionDisputeMapper.convertToResponse(savedDispute);
    }

    @Override
    public TransactionDisputeResponse updateDispute(Long id, TransactionDisputeRequest disputeRequest) {
        return disputeRepository.findById(id)
                .map(dispute -> {
                    PaymentTransaction transaction = dispute.getTransaction();

                    if (!dispute.getTransaction().getId().equals(disputeRequest.getTransactionId())) {
                        transaction = paymentTransactionRepository.findById(disputeRequest.getTransactionId())
                                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + disputeRequest.getTransactionId()));
                    }

                    TransactionDisputeMapper.updateEntityFromRequest(dispute, disputeRequest, transaction);
                    return TransactionDisputeMapper.convertToResponse(disputeRepository.save(dispute));
                })
                .orElseThrow(() -> new DisputeNotFoundException("Dispute not found with id: " + id));
    }

    @Override
    public void deleteDispute(Long id) {
        if (!disputeRepository.existsById(id)) {
            throw new DisputeNotFoundException("Dispute not found with id: " + id);
        }
        disputeRepository.deleteById(id);
    }
}
