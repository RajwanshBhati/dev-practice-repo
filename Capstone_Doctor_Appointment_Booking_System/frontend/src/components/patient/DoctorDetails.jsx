import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getDoctorById, getDoctorAvailability } from '../../api/doctor';
import { Container, Row, Col, Card, Badge, Button, Form } from 'react-bootstrap';
import { FaStar, FaMapMarkerAlt, FaPhone, FaUserMd, FaClock, FaCalendarAlt } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const DoctorDetails = () => {
  const { doctorId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [doctor, setDoctor] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [availability, setAvailability] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);

  /**
   * Load doctor details and availability.
   */
  useEffect(() => {
    // eslint-disable-next-line react-hooks/immutability
    loadDoctorDetails();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [doctorId]);

  /**
   * Load availability when date changes.
   */
  useEffect(() => {
    if (selectedDate && doctorId) {
      // eslint-disable-next-line react-hooks/immutability
      loadAvailability();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedDate]);

  /**
   * Load doctor details from API.
   */
  const loadDoctorDetails = async () => {
    setLoading(true);
    try {
      const data = await getDoctorById(doctorId);
      setDoctor(data);
      // Set default date to today
      const today = new Date().toISOString().split('T')[0];
      setSelectedDate(today);
    } catch (error) {
      console.error('Error loading doctor details:', error);
      toast.error('Failed to load doctor details');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Load availability for selected date.
   */
  const loadAvailability = async () => {
  const todayStr = new Date().toISOString().split('T')[0];
  if (selectedDate < todayStr) {
    toast.error('Selected date is in the past. Resetting to today.');
    setSelectedDate(todayStr);
    setAvailability([]);
    setSelectedSlot(null);
    return;
  }

  try {
    const data = await getDoctorAvailability(doctorId, selectedDate);
    setAvailability(data.availabilities || []);
    setSelectedSlot(null);
  } catch (error) {
    console.error('Error loading availability:', error);
    toast.error('Failed to load availability');
  }
};

  /**
   * Handle slot selection.
   */
  const handleSlotSelect = (slot) => {
    setSelectedSlot(slot);
  };

  /**
   * Navigate to booking page.
   */
  const handleBookAppointment = () => {
    if (!selectedSlot) {
      toast.error('Please select a time slot');
      return;
    }
    navigate(`/book-appointment/${doctorId}`, {
      state: {
        doctor,
        selectedDate,
        selectedSlot,
      },
    });
  };

  /**
   * Render star rating.
   */
  const renderStars = (rating) => {
    const fullStars = Math.floor(rating);
    const stars = [];
    for (let i = 0; i < fullStars; i++) {
      stars.push(<FaStar key={i} className="text-warning" style={{ fontSize: '16px' }} />);
    }
    if (stars.length === 0) {
      stars.push(<span key="no-stars" className="text-muted">No ratings</span>);
    }
    return stars;
  };

  if (loading) {
    return <Loading message="Loading doctor details..." />;
  }

  if (!doctor) {
    return (
      <Container className="mt-5 text-center">
        <h4>Doctor not found</h4>
        <Button variant="primary" onClick={() => navigate('/search-doctors')}>
          Back to Search
        </Button>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Button
        variant="outline-secondary"
        className="mb-4"
        onClick={() => navigate('/search-doctors')}
        style={{ borderRadius: '8px' }}
      >
        ← Back to Search
      </Button>

      <Row>
        {/* Doctor Profile */}
        <Col lg={8}>
          <Card className="shadow-sm mb-4" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <Row>
                <Col md={3} className="text-center">
                  <div
                    className="rounded-circle d-flex align-items-center justify-content-center mx-auto"
                    style={{
                      width: '120px',
                      height: '120px',
                      fontSize: '48px',
                      color: 'white',
                      background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                    }}
                  >
                    {doctor.full_name?.charAt(0) || 'D'}
                  </div>
                  {doctor.is_available ? (
                    <Badge bg="success" className="mt-3" style={{ fontSize: '0.9rem', padding: '6px 16px' }}>
                      <FaClock className="me-1" /> Available
                    </Badge>
                  ) : (
                    <Badge bg="secondary" className="mt-3" style={{ fontSize: '0.9rem', padding: '6px 16px' }}>
                      <FaClock className="me-1" /> Unavailable
                    </Badge>
                  )}
                </Col>
                <Col md={9}>
                  <h2 className="fw-bold mb-1" style={{ color: '#1a202c' }}>{doctor.full_name}</h2>
                  <p className="text-muted mb-2">
                    {doctor.qualification} • {doctor.specialization}
                  </p>
                  {/* <div className="mb-2">
                    {renderStars(doctor.rating)}
                    <span className="ms-2 text-muted">({doctor.total_reviews} reviews)</span>
                  </div> */}
                  <p className="text-muted mb-1">
                    <FaMapMarkerAlt className="me-2" style={{ color: '#4a90d9' }} />
                    {doctor.clinic_address}
                  </p>
                  {doctor.clinic_phone && (
                    <p className="text-muted mb-1">
                      <FaPhone className="me-2" style={{ color: '#4a90d9' }} />
                      {doctor.clinic_phone}
                    </p>
                  )}
                  <p className="text-muted mb-0">
                    <FaUserMd className="me-2" style={{ color: '#4a90d9' }} />
                    {doctor.experience_years} years experience
                  </p>
                </Col>
              </Row>

              {doctor.bio && (
                <div className="mt-4">
                  <h5 className="fw-bold">About</h5>
                  <p className="text-muted">{doctor.bio}</p>
                </div>
              )}

              <div className="mt-3">
                <h5 className="fw-bold">Consultation Fee</h5>
                <h3 className="text-primary fw-bold">${doctor.consultation_fee}</h3>
              </div>
            </Card.Body>
          </Card>
        </Col>

        {/* Booking Section */}
        <Col lg={4}>
          <Card className="shadow-sm booking-card" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-3">Book Appointment</h5>

              {/* Date Selection */}
              <div className="mb-3">
                <Form.Label className="fw-semibold text-secondary">
                  <FaCalendarAlt className="me-2" /> Select Date
                </Form.Label>
                <Form.Control
                  type="date"
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                  min={new Date().toISOString().split('T')[0]}
                  style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
              </div>

              {/* Time Slots */}
              <div className="mb-3">
                <Form.Label className="fw-semibold text-secondary">
                  <FaClock className="me-2" /> Available Time Slots
                </Form.Label>
                {availability.length === 0 ? (
                  <p className="text-muted text-center py-3">No slots available on this date</p>
                ) : (
                  <div className="d-flex flex-wrap gap-2">
                    {availability.map((slot) => (
                      <Button
                        key={slot.id}
                        variant={selectedSlot?.id === slot.id ? 'primary' : 'outline-primary'}
                        size="sm"
                        className="px-3 py-2"
                        style={{ borderRadius: '8px' }}
                        onClick={() => handleSlotSelect(slot)}
                        disabled={!slot.is_available}
                      >
                        {slot.start_time} - {slot.end_time}
                      </Button>
                    ))}
                  </div>
                )}
              </div>

              <Button
                variant="primary"
                className="w-100 py-3 fw-semibold"
                style={{ borderRadius: '10px' }}
                onClick={handleBookAppointment}
                disabled={!selectedSlot}
              >
                {selectedSlot ? 'Proceed to Book' : 'Select a Time Slot'}
              </Button>

              <p className="text-center text-muted mt-2" style={{ fontSize: '0.8rem' }}>
                <FaClock className="me-1" /> Appointments are 30 minutes long
              </p>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DoctorDetails;
