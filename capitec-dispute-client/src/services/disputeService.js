import apiClient from './apiClient';

export const disputeService = {
  getTransaction: async (transactionId) => {
    try {
      const response = await apiClient.get(`/transactions/${transactionId}`);
      return response.data;
    } catch (error) {
      console.error('Failed to fetch transaction:', error);
      throw new Error('Failed to load transaction details.');
    }
  },

  createDispute: async (disputeData, transactionId) => {
    try {
      const response = await apiClient.post('/disputes', {
        ...disputeData,
        transactionId: parseInt(transactionId)
      });
      return response.data;
    } catch (error) {
      console.error('Failed to create dispute:', error);
      throw new Error(error.response?.data?.error || 'Failed to create dispute. Please try again.');
    }
  },

  getDispute: async (disputeId) => {
    try {
      const response = await apiClient.get(`/disputes/${disputeId}`);
      return response.data;
    } catch (error) {
      console.error('Failed to fetch dispute:', error);
      throw new Error('Failed to load dispute details.');
    }
  },

  getDisputesByTransactionId: async (transactionId) => {
    try {
      const response = await apiClient.get(`/disputes/transaction/${transactionId}`);
      return response.data;
    } catch (error) {
      console.error('Failed to fetch disputes for transaction:', error);
      // Return empty array if no disputes found (404) or other errors
      if (error.response?.status === 404) {
        return [];
      }
      throw new Error('Failed to load disputes for transaction.');
    }
  },

  updateDispute: async (disputeId, disputeData) => {
    try {
      const response = await apiClient.put(`/disputes/${disputeId}`, disputeData);
      return response.data;
    } catch (error) {
      console.error('Failed to update dispute:', error);
      
      // Get more specific error message from backend
      let errorMessage = 'Failed to update dispute. Please try again.';
      if (error.response?.data) {
        if (typeof error.response.data === 'string') {
          errorMessage = error.response.data;
        } else if (error.response.data.message) {
          errorMessage = error.response.data.message;
        } else if (error.response.data.error) {
          errorMessage = error.response.data.error;
        }
      }
      
      throw new Error(errorMessage);
    }
  },

  getDisputeWithTransaction: async (disputeId) => {
    try {
      const dispute = await disputeService.getDispute(disputeId);
      let transaction = null;
      
      if (dispute.transactionId) {
        try {
          transaction = await disputeService.getTransaction(dispute.transactionId);
        } catch (transactionError) {
          console.error('Failed to fetch transaction details:', transactionError);
        }
      }
      
      return { dispute, transaction };
    } catch (error) {
      throw error;
    }
  }
};
