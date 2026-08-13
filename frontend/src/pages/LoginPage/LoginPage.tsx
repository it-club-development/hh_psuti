import React, { useState } from 'react';
import './LoginPage.css';

const LoginPage: React.FC = () => {
  const [accepted, setAccepted] = useState(false);

  const handleAgreementClick = (e: React.MouseEvent) => {
    e.preventDefault();
    alert('Здесь будет текст пользовательского соглашения');
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h2>Пользовательское соглашение</h2>

        <p className="agreement-text">
          Уважаемый пользователь!<br />
          Для входа в личный кабинет вам необходимо принять{' '}
          {/* ССЫЛКА НА ДОКУМЕНТ ДОБАВИТЬ  */}
          <a 
            href="#" 
            className="agreement-link"
            onClick={handleAgreementClick}
          >
            условия пользовательского соглашения
          </a>
        .
        </p>

        <label className="checkbox-label">
          <input
            type="checkbox"
            checked={accepted}
            onChange={(e) => setAccepted(e.target.checked)}
            className="custom-checkbox"
          />
          <span className="checkmark"></span>
          Принимаю пользовательское соглашение
        </label>

        <div className="button-group">
          <button className="btn-cancel">Отмена</button>
          <button
            className={`btn-accept ${accepted ? 'active' : ''}`}
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