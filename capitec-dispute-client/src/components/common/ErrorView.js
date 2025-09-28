import React from 'react';

const ErrorView = ({ error, onRetry }) => {
  return (
    <div className="error-message">
      {error}
      <button onClick={onRetry} className="retry-button">
        Retry
      </button>
    </div>
  );
};

export default ErrorView;
