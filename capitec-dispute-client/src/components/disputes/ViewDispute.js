import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { disputeService } from '../../services';
import { DisputeViewComponent } from './';
import './Disputes.css';
import logo from '../../logo.svg';

const ViewDispute = () => {
  const { disputeId } = useParams();
  const navigate = useNavigate();
  const [dispute, setDispute] = useState(null);
  const [transaction, setTransaction] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [disputeData, setDisputeData] = useState({
    subject: '',
    description: '',
    status: 'OPEN'
  });
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDisputeAndTransaction = async () => {
      try {
        const { dispute: disputeData, transaction: transactionData } = await disputeService.getDisputeWithTransaction(disputeId);
        setDispute(disputeData);
        setTransaction(transactionData);
        setDisputeData({
          subject: disputeData.subject,
          description: disputeData.description,
          status: disputeData.status
        });
      } catch (error) {
        setError(error.message);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDisputeAndTransaction();
  }, [disputeId]);

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
      const updateData = {
        ...disputeData,
        transactionId: dispute.transactionId
      };

      const updatedDispute = await disputeService.updateDispute(disputeId, updateData);
      setDispute(updatedDispute);
      setIsEditing(false);
    } catch (error) {
      setError(error.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancelEdit = () => {
    setDisputeData({
      subject: dispute.subject,
      description: dispute.description,
      status: dispute.status
    });
    setIsEditing(false);
    setError('');
  };

  const handleEdit = () => {
    setIsEditing(true);
  };

  if (isLoading) {
    return (
      <div className="dispute-container">
        <div className="loading">Loading dispute details...</div>
      </div>
    );
  }

  if (!dispute) {
    return (
      <div className="dispute-container">
        <div className="error-message">Dispute not found.</div>
      </div>
    );
  }

  return (
    <div className="dispute-container">
      <div className="dispute-header">
        <div className="header-left">
          <img src={logo} alt="Capitec" className="capitec-logo" />
          <h1>View Dispute</h1>
        </div>
        <div className="header-actions">
          <button onClick={() => navigate('/transactions')} className="back-button">
            ← Back to Transactions
          </button>
        </div>
      </div>

      {/* Transaction Details */}
      <div className="transaction-details">
        <h2>Transaction Details</h2>
        <div className="dispute-details">
          <div className="detail-item">
            <label>Reference:</label>
            <span>{dispute.transactionReference}</span>
          </div>
          <div className="detail-item">
            <label>Amount:</label>
            <span>{new Intl.NumberFormat('en-ZA', { style: 'currency', currency: 'ZAR' }).format(dispute.transactionAmount)}</span>
          </div>
          <div className="detail-item">
            <label>Transaction Date:</label>
            <span>{transaction ? new Date(transaction.transactionDate).toLocaleDateString('en-ZA') : 'N/A'}</span>
          </div>
          <div className="detail-item">
            <label>Merchant:</label>
            <span>{transaction ? transaction.merchantName : dispute.merchantName || 'N/A'}</span>
          </div>
          <div className="detail-item">
            <label>Description:</label>
            <span>{transaction ? transaction.description : dispute.transactionDescription || 'N/A'}</span>
          </div>
        </div>
      </div>

      <DisputeViewComponent
        dispute={dispute}
        isEditing={isEditing}
        disputeData={disputeData}
        onInputChange={handleInputChange}
        onSubmit={handleSubmit}
        onEdit={handleEdit}
        onCancel={handleCancelEdit}
        isSubmitting={isSubmitting}
        error={error}
      />
    </div>
  );
};

export default ViewDispute;
