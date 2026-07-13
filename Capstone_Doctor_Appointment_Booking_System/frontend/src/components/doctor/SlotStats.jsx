import React from 'react';
import { Row, Col, Card } from 'react-bootstrap';
import { FaCalendarPlus, FaCalendarCheck, FaCalendarTimes } from 'react-icons/fa';

const SlotStats = ({ stats, loading }) => {
  if (loading) {
    return (
      <Row>
        {[1, 2, 3].map((i) => (
          <Col lg={4} md={6} key={i} className="mb-3">
            <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
              <Card.Body className="p-4">
                <div className="placeholder-glow">
                  <span className="placeholder w-100" style={{ height: '60px' }} />
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    );
  }

  return (
    <Row>
      <Col lg={4} md={6} className="mb-3">
        <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
          <Card.Body className="p-4">
            <div className="d-flex justify-content-between align-items-start">
              <div>
                <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                  Total Slots
                </p>
                <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                  {stats?.total_slots || 0}
                </h2>
              </div>
              <div
                className="rounded-circle d-flex align-items-center justify-content-center"
                style={{
                  width: '48px',
                  height: '48px',
                  background: 'rgba(74, 144, 217, 0.1)',
                  color: '#4a90d9',
                  fontSize: '20px',
                }}
              >
                <FaCalendarPlus />
              </div>
            </div>
          </Card.Body>
        </Card>
      </Col>

      <Col lg={4} md={6} className="mb-3">
        <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
          <Card.Body className="p-4">
            <div className="d-flex justify-content-between align-items-start">
              <div>
                <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                  Available Slots
                </p>
                <h2 className="fw-bold mb-0" style={{ color: '#48bb78' }}>
                  {stats?.available_slots || 0}
                </h2>
              </div>
              <div
                className="rounded-circle d-flex align-items-center justify-content-center"
                style={{
                  width: '48px',
                  height: '48px',
                  background: 'rgba(72, 187, 120, 0.1)',
                  color: '#48bb78',
                  fontSize: '20px',
                }}
              >
                <FaCalendarCheck />
              </div>
            </div>
          </Card.Body>
        </Card>
      </Col>

      <Col lg={4} md={6} className="mb-3">
        <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
          <Card.Body className="p-4">
            <div className="d-flex justify-content-between align-items-start">
              <div>
                <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                  Booked Slots
                </p>
                <h2 className="fw-bold mb-0" style={{ color: '#dc3545' }}>
                  {stats?.booked_slots || 0}
                </h2>
              </div>
              <div
                className="rounded-circle d-flex align-items-center justify-content-center"
                style={{
                  width: '48px',
                  height: '48px',
                  background: 'rgba(220, 53, 69, 0.1)',
                  color: '#dc3545',
                  fontSize: '20px',
                }}
              >
                <FaCalendarTimes />
              </div>
            </div>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default SlotStats;
