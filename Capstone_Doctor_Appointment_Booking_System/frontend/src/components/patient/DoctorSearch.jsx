/**
 * Doctor Search page component.
 * Allows patients to search for doctors with various filters.
 */

import { useState, useEffect,useRef,useMemo} from 'react';
import { useNavigate } from 'react-router-dom';
import { searchDoctors, getSpecializations } from '../../api/doctor';
import { Form, Button, Card, Row, Col, Container, Badge } from 'react-bootstrap';
import { FaSearch, FaMapMarkerAlt, FaStar, FaClock } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';


function debounce(fn, delay) {
  let timer;
  const debounced = (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
  debounced.cancel = () => clearTimeout(timer);
  return debounced;
}

const DoctorSearch = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);
  const [doctors, setDoctors] = useState([]);
  const [specializations, setSpecializations] = useState([]);
  const [specializationsError, setSpecializationsError] = useState(false);
  const [total, setTotal] = useState(0);
   const [searchText, setSearchText] = useState({
    query: '', location: '', min_experience: '', max_fee: '',
  });


  const [hasMore, setHasMore] = useState(false);
  const [filters, setFilters] = useState({
    query: '',
    specialization: '',
    location: '',
    min_experience: '',
    max_fee: '',
    limit: 10,
    skip: 0,
  });

  const isLoadMoreRef = useRef(false);
  const debouncedCommit = useMemo(
    () => debounce((name, value) => {
      setFilters((prev) => ({ ...prev, [name]: value, skip: 0 }));
    }, 400),
    []
  );
  useEffect(() => () => debouncedCommit.cancel(), [debouncedCommit]);

  /**
   * Load specializations from the API.
   */
  const loadSpecializations = async () => {
    setSpecializationsError(false);
    try {
      const data = await getSpecializations();
      setSpecializations(data.specializations || []);
    } catch (error) {
      console.error('Error loading specializations:', error);
      toast.error('Failed to load specializations');
      setSpecializationsError(true);
    }
  };

  /**
   * Load doctors with current filters.
   */
  const loadDoctors = async (isLoadMore = false) => {
    isLoadMore ? setLoadingMore(true) : setLoading(true);
    try {
      const params = { ...filters };
      Object.keys(params).forEach((key) => {
        if (params[key] === '' || params[key] === null || params[key] === undefined) {
          delete params[key];
        }
      });
      const data = await searchDoctors(params);
      setDoctors((prev) => (isLoadMore ? [...prev, ...(data.doctors || [])] : data.doctors || []));
      setTotal(data.total || 0);
      setHasMore(data.has_more || false);
    } catch (error) {
      console.error('Error searching doctors:', error);
      toast.error('Failed to search doctors');
    } finally {
      isLoadMore ? setLoadingMore(false) : setLoading(false);
    }
};

  /**
   * Load specializations on component mount.
   */
  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadSpecializations();
  }, []);

  /**
   * Load doctors on filter changes.
   */
  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadDoctors(isLoadMoreRef.current);
    isLoadMoreRef.current = false;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters]);

  /**
   * Handle filter input changes.
   */
  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    if (name === 'specialization' || name === 'min_rating') {
      setFilters((prev) => ({ ...prev, [name]: value, skip: 0 }));
      return;
    }
    setSearchText((prev) => ({ ...prev, [name]: value }));
    debouncedCommit(name, value);
  };

  /**
   * Handle search form submission.
   */
  const handleSearch = (e) => {
    e.preventDefault();
    debouncedCommit.cancel();
    setFilters((prev) => ({ ...prev, ...searchText, skip: 0 }));
  };

  /**
   * Load more doctors for pagination.
   */
  const handleLoadMore = () => {
    isLoadMoreRef.current = true;
    setFilters((prev) => ({ ...prev, skip: prev.skip + prev.limit }));
  };

  /**
   * Navigate to doctor details page.
   */
  const handleDoctorClick = (doctorId) => {
    navigate(`/doctors/${doctorId}`);
  };

  if (loading && doctors.length === 0) {
    return <Loading message="Searching for doctors..." />;
  }

  return (
    <Container className="mt-4">
      <h1 className="mb-4">Find a Doctor</h1>
      <p className="text-muted mb-4">Search for doctors by name, specialization, or location.</p>

      {/* Search Form */}
      <Card className="shadow-sm mb-4" style={{ borderRadius: '12px', border: 'none' }}>
        <Card.Body className="p-4">
          <Form onSubmit={handleSearch}>
            <Row>
              <Col lg={4} md={6} className="mb-3">
                <Form.Control
                  type="text"
                  name="query"
                  placeholder="Search by name, specialization..."
                  value={filters.query}
                  onChange={handleFilterChange}
                  style={{ padding: '0.7rem 1rem', borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
              </Col>
              <Col lg={3} md={6} className="mb-3">
                <Form.Select
                  name="specialization"
                  value={filters.specialization}
                  onChange={handleFilterChange}
                  disabled={specializationsError}
                  style={{ padding: '0.7rem 1rem', borderRadius: '8px', border: '2px solid #e2e8f0' }}
                >
                   <option value="">
                    {specializationsError ? 'Unable to load specializations' : 'All Specializations'}
                   </option>
                  {specializations.map((spec) => (
                    <option key={spec} value={spec}>{spec}</option>
                   ))}
                  </Form.Select>
                  {specializationsError && (
                    <Button variant="link" size="sm" className="p-0 mt-1" onClick={loadSpecializations}>
                      Retry loading specializations
                    </Button>
                  )}
                </Col>
              <Col lg={2} md={6} className="mb-3">
                <Form.Control
                  type="text"
                  name="location"
                  placeholder="Location"
                  value={filters.location}
                  onChange={handleFilterChange}
                  style={{ padding: '0.7rem 1rem', borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
              </Col>
              <Col lg={2} md={6} className="mb-3">
                <Form.Control
                  type="number"
                  name="max_fee"
                  placeholder="Max Fee ($)"
                  value={filters.max_fee}
                  onChange={handleFilterChange}
                  style={{ padding: '0.7rem 1rem', borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
              </Col>
              <Col lg={1} className="mb-3">
                <Button type="submit" variant="primary" className="w-100" style={{ borderRadius: '8px', padding: '0.7rem' }}>
                  <FaSearch />
                </Button>
              </Col>
            </Row>
            <Row>
              <Col md={3} className="mb-2">
                <Form.Control
                  type="number"
                  name="min_experience"
                  placeholder="Min Experience (yrs)"
                  value={filters.min_experience}
                  onChange={handleFilterChange}
                  style={{ padding: '0.7rem 1rem', borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
              </Col>
              <Col md={3} className="mb-2">
                <Form.Select
                  name="min_rating"
                  value={filters.min_rating}
                  onChange={handleFilterChange}
                  style={{ padding: '0.7rem 1rem', borderRadius: '8px', border: '2px solid #e2e8f0' }}
                >
                  <option value="">Any Rating</option>
                  <option value="4">4+ Stars</option>
                  <option value="3">3+ Stars</option>
                  <option value="2">2+ Stars</option>
                  <option value="1">1+ Star</option>
                </Form.Select>
              </Col>
              <Col md={6} className="d-flex align-items-center">
                <span className="text-muted" style={{ fontSize: '0.9rem' }}>
                  Found <strong>{total}</strong> {total === 1 ? 'doctor' : 'doctors'}
                </span>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      {/* Doctor List */}
      {doctors.length === 0 ? (
        <div className="text-center py-5">
          <div style={{ fontSize: '48px', marginBottom: '20px' }}>🔍</div>
          <h4>No doctors found</h4>
          <p className="text-muted">Try adjusting your search filters or search terms.</p>
          <Button
            variant="outline-primary"
            onClick={() => {
              debouncedCommit.cancel();
              const cleared = { query: '', specialization: '', location: '', min_experience: '', max_fee: '', min_rating: '', limit: 10, skip: 0 };
              setSearchText({ query: '', location: '', min_experience: '', max_fee: '' });
              setFilters(cleared);
            }}
          >
            Clear Filters
          </Button>
        </div>
      ) : (
        <>
          {doctors.map((doctor) => (
            <Card
              key={doctor.id}
              className="mb-3 shadow-sm doctor-card"
              onClick={() => handleDoctorClick(doctor.id)}
              style={{
                cursor: 'pointer',
                borderRadius: '12px',
                border: 'none',
                transition: 'transform 0.2s, box-shadow 0.2s',
              }}
            >
              <Card.Body className="p-4">
                <Row>
                  <Col md={2} className="text-center">
                    <div
                      className="rounded-circle bg-primary d-flex align-items-center justify-content-center mx-auto"
                      style={{
                        width: '80px',
                        height: '80px',
                        fontSize: '32px',
                        color: 'white',
                        background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                      }}
                    >
                      {doctor.full_name?.charAt(0) || 'D'}
                    </div>
                    {doctor.is_available ? (
                      <Badge bg="success" className="mt-2" style={{ fontSize: '0.8rem', padding: '4px 12px' }}>
                        <FaClock className="me-1" /> Available
                      </Badge>
                    ) : (
                      <Badge bg="secondary" className="mt-2" style={{ fontSize: '0.8rem', padding: '4px 12px' }}>
                        <FaClock className="me-1" /> Unavailable
                      </Badge>
                    )}
                  </Col>
                  <Col md={6}>
                    <h5 className="fw-bold mb-1" style={{ color: '#1a202c' }}>{doctor.full_name}</h5>
                    <p className="text-muted mb-1" style={{ fontSize: '0.9rem' }}>
                      {doctor.qualification} • {doctor.specialization}
                    </p>
                    <p className="text-muted mb-1" style={{ fontSize: '0.9rem' }}>
                      <FaMapMarkerAlt className="me-1" style={{ color: '#4a90d9' }} /> {doctor.clinic_address}
                    </p>
                    <p className="text-muted mb-0" style={{ fontSize: '0.9rem' }}>
                      {doctor.experience_years} years experience
                    </p>
                  </Col>
                  <Col md={4} className="text-end">
                    {/* <div className="mb-2">
                      {renderStars(doctor.rating)}
                      <span className="ms-1 text-muted" style={{ fontSize: '0.85rem' }}>
                        ({doctor.total_reviews})
                      </span>
                    </div> */}
                    <h4 className="text-primary fw-bold" style={{ fontSize: '1.5rem' }}>
                      ${doctor.consultation_fee}
                    </h4>
                    <Button
                      variant="outline-primary"
                      size="sm"
                      className="mt-2"
                      style={{ borderRadius: '8px', padding: '0.4rem 1.5rem' }}
                      onClick={(e) => {
                        e.stopPropagation();
                        handleDoctorClick(doctor.id);
                      }}
                    >
                      View Profile
                    </Button>
                  </Col>
                </Row>
              </Card.Body>
            </Card>
          ))}

          {/* Load More */}
          {hasMore && (
            <div className="text-center mt-4">
              <Button
                variant="outline-primary"
                onClick={handleLoadMore}
                disabled={loadingMore}
                style={{ borderRadius: '8px', padding: '0.6rem 2rem' }}
              >
                {loading ? 'Loading...' : 'Load More'}
              </Button>
            </div>
          )}
        </>
      )}
    </Container>
  );
};

export default DoctorSearch;
