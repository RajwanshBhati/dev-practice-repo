import { getPasswordChecklist } from '../../utils/validators';
import { FaCheckCircle, FaCircle } from 'react-icons/fa';

const PasswordRequirements = ({ password }) => {
  const checklist = getPasswordChecklist(password);

  return (
    <ul className="list-unstyled mt-2 mb-0" style={{ fontSize: '0.8rem' }}>
      {checklist.map((req) => (
        <li key={req.id} className={req.met ? 'text-success' : 'text-muted'}>
          {req.met ? <FaCheckCircle className="me-1" /> : <FaCircle className="me-1" style={{ fontSize: '6px', verticalAlign: 'middle' }} />}
          {req.label}
        </li>
      ))}
    </ul>
  );
};

export default PasswordRequirements;
