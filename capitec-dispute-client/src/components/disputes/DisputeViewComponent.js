import React from 'react';

const DisputeViewComponent = ({
  dispute,
  isEditing,
  disputeData,
  onInputChange,
  onSubmit,
  onEdit,
  onCancel,
  isSubmitting,
  error
}) => {
  if (isEditing) {
    return (
      <div className="dispute-form-container">
        <h2>Edit Dispute Information</h2>
        <form onSubmit={onSubmit} className="dispute-form">
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
              onChange={onInputChange}
              required
              disabled={isSubmitting}
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={disputeData.description}
              onChange={onInputChange}
              required
              disabled={isSubmitting}
              rows="6"
            />
          </div>

          <div className="form-group">
            <label htmlFor="status">Status</label>
            <select
              id="status"
              name="status"
              value={disputeData.status}
              onChange={onInputChange}
              disabled={isSubmitting}
            >
              <option value="OPEN">Open</option>
              <option value="PENDING">Pending</option>
              <option value="RESOLVED">Resolved</option>
            </select>
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={onCancel}
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
              {isSubmitting ? 'Updating Dispute...' : 'Update Dispute'}
            </button>
          </div>
        </form>
      </div>
    );
  }

  return (
    <div className="dispute-form-container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>Dispute Information</h2>
        <button onClick={onEdit} className="edit-button">
          Edit Dispute
        </button>
      </div>

      <div className="dispute-details">
        <div className="detail-item">
          <label>Subject:</label>
          <span>{dispute.subject}</span>
        </div>
        <div className="detail-item">
          <label>Description:</label>
          <span>{dispute.description}</span>
        </div>
        <div className="detail-item">
          <label>Status:</label>
          <div className="status-progress-bar">
            <div className="progress-step">
              <div className={`progress-circle ${dispute.status === 'OPEN' || dispute.status === 'PENDING' || dispute.status === 'RESOLVED' ? 'active' : ''}`}>
                <span className="step-number">1</span>
              </div>
              <span className="step-label">Open</span>
            </div>
            <div className={`progress-line ${dispute.status === 'PENDING' || dispute.status === 'RESOLVED' ? 'active' : ''}`}></div>
            <div className="progress-step">
              <div className={`progress-circle ${dispute.status === 'PENDING' || dispute.status === 'RESOLVED' ? 'active' : ''}`}>
                <span className="step-number">2</span>
              </div>
              <span className="step-label">Pending</span>
            </div>
            <div className={`progress-line ${dispute.status === 'RESOLVED' ? 'active' : ''}`}></div>
            <div className="progress-step">
              <div className={`progress-circle ${dispute.status === 'RESOLVED' ? 'active' : ''}`}>
                <span className="step-number">3</span>
              </div>
              <span className="step-label">Resolved</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DisputeViewComponent;
