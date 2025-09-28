import React, { useState, useEffect } from 'react';
import './Transactions.css';
import { authService, transactionService } from '../../services';
import TransactionViewComponent from './TransactionViewComponent';
import { LoadingView, ErrorView } from '../common';
import { useNavigate } from 'react-router-dom';
import logo from '../../logo.svg';

const TransactionHeader = ({ userInfo, handleLogout }) => {
  return (
    <header className="transactions-header">
      <div className="header-content">
        <div className="header-left">
          <img src={logo} alt="Capitec" className="header-logo" />
          <h1>Transaction Dispute Portal</h1>
        </div>
        {userInfo && (
          <div className="user-info">
            <span className="welcome-text">Welcome, {userInfo.username}</span>
            <button onClick={handleLogout} className="logout-button">
              <svg className="button-icon" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clipRule="evenodd" />
              </svg>
              Logout
            </button>
          </div>
        )}
      </div>
    </header>
  );
};

const Transactions = () => {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!authService.isAuthenticated()) {
      navigate('/login');
      return;
    }

    const user = authService.getUserInfo();
    setUserInfo(user);

    fetchTransactions();
  }, [navigate]);

  const fetchTransactions = async () => {
    try {
      setIsLoading(true);
      setError('');
      const transactionData = await transactionService.getTransactions();
      setTransactions(transactionData);
    } catch (error) {
      if (error.message === 'UNAUTHORIZED') {
        handleLogout();
        return;
      }
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  if (isLoading) {
    return <LoadingView />;
  }

  return (
    <div className="transactions-container">
      <TransactionHeader userInfo={userInfo} handleLogout={handleLogout} />

      <main className="transactions-main">
        {error && (
          <ErrorView error={error} onRetry={fetchTransactions} />
        )}

        <TransactionViewComponent
          transactions={transactions}
          isLoading={isLoading}
        />
      </main>
    </div>
  );
};

export default Transactions;
