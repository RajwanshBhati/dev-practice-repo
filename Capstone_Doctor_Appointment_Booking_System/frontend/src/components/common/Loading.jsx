/**
 * Loading spinner component.
 * Displays a centered spinner with an optional message.
 * Used to indicate loading states throughout the application.
 *
 * @param {Object} props - Component props
 * @param {string} props.message - Message to display below the spinner (default: "Loading...")
 * @returns {JSX.Element} Loading component
 */

import { Spinner } from 'react-bootstrap';

const Loading = ({ message = 'Loading...' }) => {
  return (
    <div className="d-flex flex-column align-items-center justify-content-center py-5">
      <Spinner animation="border" variant="primary" />
      <p className="mt-3 text-muted">{message}</p>
    </div>
  );
};

export default Loading;
