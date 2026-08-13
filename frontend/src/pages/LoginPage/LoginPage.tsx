import React, { useState } from 'react';
import './LoginPage.css';

const LoginPage: React.FC = () => {
  const [accepted, setAccepted] = useState(false);

  const handleAccept = () => {
    if (accepted) {
      alert('Вход выполнен!');
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h2>Пользовательское соглашение</h2>

        <p className="agreement-text">
          Уважаемый пользователь!<br />
          Для входа в личный кабинет вам необходимо принять условия пользовательского соглашения.
        </p>

        <label className="checkbox-label">
          <input
            type="checkbox"
            checked={accepted}
            onChange={(e) => setAccepted(e.target.checked)}
          />
          Принимаю пользовательское соглашение
        </label>

        <div className="button-group">
          <button className="btn-cancel">Отмена</button>
          <button
            className={`btn-accept ${accepted ? 'active' : ''}`}
            onClick={handleAccept}
            disabled={!accepted}
          >
            Принять
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;