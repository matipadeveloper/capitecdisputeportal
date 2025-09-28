import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Login, Transactions, CreateDispute, ViewDispute } from './components';
import { authService } from './services';
import './App.css';

const ProtectedRoute = ({ children }) => {
  return authService.isAuthenticated() ? children : <Navigate to="/login" replace />;
};

function App() {
  return (
    <Router basename="/portal">
      <div className="App">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />

          <Route path="/login" element={<Login />} />

          <Route path="/transactions" element={
                  <ProtectedRoute>
                    <Transactions />
                  </ProtectedRoute>
                }
          />

          <Route path="/disputes/create/:transactionId" element={
              <ProtectedRoute>
                <CreateDispute />
              </ProtectedRoute>
            }
          />

          <Route
            path="/disputes/view/:disputeId"
            element={
              <ProtectedRoute>
                <ViewDispute />
              </ProtectedRoute>
            }
          />

          <Route path="*" element={<Navigate to="/login" replace />} />

        </Routes>
      </div>
    </Router>
  );
}

export default App;
