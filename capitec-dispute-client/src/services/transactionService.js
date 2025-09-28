import apiClient from './apiClient';
import { disputeService } from './disputeService';

export const transactionService = {
  getTransactions: async () => {
    try {
      const response = await apiClient.get('/transactions');
      const transactions = response.data;

      // Fetch dispute for each transaction (one-to-one relationship)
      const transactionsWithDisputes = await Promise.all(
        transactions.map(async (transaction) => {
          try {
            const disputes = await disputeService.getDisputesByTransactionId(transaction.id);
            return {
              ...transaction,
              dispute: disputes.length > 0 ? disputes[0] : null // Only one dispute per transaction
            };
          } catch (error) {
            console.error(`Failed to fetch dispute for transaction ${transaction.id}:`, error);
            return {
              ...transaction,
              dispute: null
            };
          }
        })
      );

      return transactionsWithDisputes;
    } catch (error) {
      console.error('Failed to fetch transactions:', error);

      if (error.response?.status === 401) {
        throw new Error('UNAUTHORIZED');
      }

      throw new Error('Failed to load transactions. Please try again.');
    }
  },

};
