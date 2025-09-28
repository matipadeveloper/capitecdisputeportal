import React from 'react';
import { useNavigate } from 'react-router-dom';

const TransactionViewComponent = ({
  transactions,
  isLoading
}) => {
  const navigate = useNavigate();

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-ZA', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  const formatAmount = (amount) => {
    return new Intl.NumberFormat('en-ZA', {
      style: 'currency',
      currency: 'ZAR'
    }).format(amount);
  };

  return (
    <div>
      {transactions.length === 0 ? (
        <div className="no-transactions">
          <p>No transactions found.</p>
        </div>
      ) : (
        <div className="transactions-table-container">
          <table className="transactions-table">
            <thead>
              <tr>
                <th>Reference</th>
                <th>Date</th>
                <th>Description</th>
                <th>Merchant</th>
                <th>Amount</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((transaction) => (
                <tr key={transaction.id}>
                  <td className="reference">{transaction.transactionReference}</td>
                  <td>{formatDate(transaction.transactionDate)}</td>
                  <td className="description">{transaction.description}</td>
                  <td>{transaction.merchantName}</td>
                  <td className="amount">{formatAmount(transaction.amount)}</td>
                  <td>
                    {transaction.dispute ? (
                      <button
                        className="dispute-button view-dispute"
                        onClick={() => navigate(`/disputes/view/${transaction.dispute.id}`)}
                      >
                        View Dispute
                      </button>
                    ) : (
                      <button
                        className="dispute-button create-dispute"
                        onClick={() => navigate(`/disputes/create/${transaction.id}`)}
                      >
                        Create Dispute
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default TransactionViewComponent;
