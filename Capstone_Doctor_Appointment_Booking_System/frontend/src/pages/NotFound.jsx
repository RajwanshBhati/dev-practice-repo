import { Link } from 'react-router-dom';
import { Container, Row, Col, Button } from 'react-bootstrap';

const NotFound = () => {
  return (
    <Container className="mt-5">
      <Row className="justify-content-center text-center">
        <Col md={6}>
          <h1 className="display-1 text-primary">404</h1>
          <h2 className="mb-3">Page Not Found</h2>
          <p className="text-muted mb-4">
            The page you are looking for does not exist or has been moved.
          </p>
          <Button as={Link} to="/" variant="primary">
            Go Home
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

export default NotFound;
