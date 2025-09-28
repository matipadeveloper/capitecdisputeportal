import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { disputeService } from '../../services';
import './Disputes.css';
import logo from '../../logo.svg';

const CreateDispute = () => {
  const { transactionId } = useParams();
  const navigate = useNavigate();
  const [transaction, setTransaction] = useState(null);
  const [disputeData, setDisputeData] = useState({
    subject: '',
    description: '',
    status: 'OPEN'
  });
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchTransaction = async () => {
      try {
        const transactionData = await disputeService.getTransaction(transactionId);
        setTransaction(transactionData);
      } catch (error) {
        setError(error.message);
      } finally {
        setIsLoading(false);
      }
    };

    fetchTransaction();
  }, [transactionId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setDisputeData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError('');

    try {
      const response = await disputeService.createDispute(disputeData, transactionId);
      navigate(`/disputes/view/${response.id}`);
    } catch (error) {
      setError(error.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    navigate('/transactions');
  };

  if (isLoading) {
    return (
      <div className="dispute-container">
        <div className="loading">Loading transaction details...</div>
      </div>
    );
  }

  return (
    <div className="dispute-container">
      <div className="dispute-header">
        <div className="header-left">
          <img src={logo} alt="Capitec" className="logo" />
          <h1>Create Dispute</h1>
        </div>
        <div className="header-actions">
          <button onClick={() => navigate('/transactions')} className="back-button">
            ← Back to Transactions
          </button>
        </div>
      </div>

      {transaction && (
        <div className="transaction-details">
          <h2>Transaction Details</h2>
          <div className="dispute-details">
            <div className="detail-item">
              <label>Reference:</label>
              <span>{transaction.transactionReference}</span>
            </div>
            <div className="detail-item">
              <label>Amount:</label>
              <span>{new Intl.NumberFormat('en-ZA', { style: 'currency', currency: 'ZAR' }).format(transaction.amount)}</span>
            </div>
            <div className="detail-item">
              <label>Transaction Date:</label>
              <span>{new Date(transaction.transactionDate).toLocaleDateString('en-ZA')}</span>
            </div>
            <div className="detail-item">
              <label>Merchant:</label>
              <span>{transaction.merchantName}</span>
            </div>
            <div className="detail-item">
              <label>Description:</label>
              <span>{transaction.description}</span>
            </div>
          </div>
        </div>
      )}

      <div className="dispute-form-container">
        <h2>Dispute Information</h2>
        <form onSubmit={handleSubmit} className="dispute-form">
          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="subject">Subject</label>
            <input
              type="text"
              id="subject"
              name="subject"
              value={disputeData.subject}
              onChange={handleInputChange}
              required
              disabled={isSubmitting}
              placeholder="Brief description of the dispute"
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={disputeData.description}
              onChange={handleInputChange}
              required
              disabled={isSubmitting}
              placeholder="Detailed explanation of why you are disputing this transaction"
              rows="6"
            />
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={handleCancel}
              className="cancel-button"
              disabled={isSubmitting}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="submit-button"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Creating Dispute...' : 'Create Dispute'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateDispute;
