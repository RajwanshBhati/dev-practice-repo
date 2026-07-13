import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Navbar, Nav, Container, NavDropdown } from 'react-bootstrap';
import { FaUserMd, FaUser, FaSignOutAlt, FaUserPlus, FaSignInAlt, FaHome } from 'react-icons/fa';

const AppNavbar = () => {
  const { user, isAuthenticated, isPatient, isDoctor, isAdmin, logout,loading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  const homePath = isAdmin ? '/admin/dashboard' : isDoctor ? '/doctor/dashboard' : isPatient ? '/home' : '/';


  return (
    <Navbar expand="lg" className="navbar-custom py-3" style={{ background: 'white', boxShadow: '0 2px 20px rgba(0,0,0,0.06)' }}>
      <Container fluid className="px-4">
        <Navbar.Brand as={Link} to="/" className="fw-bold d-flex align-items-center">
          <span className="brand-icon bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2"
                style={{ width: '40px', height: '40px', fontSize: '20px' }}>
            <FaUserMd />
          </span>
          <span style={{ color: '#1a202c', fontSize: '1.3rem', fontWeight: 700 }}>
            Health<span style={{ color: '#4a90d9' }}>Book</span>
          </span>
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto align-items-lg-center">
            {loading ? null : isAuthenticated ? (
              <>
                <Nav.Link as={Link} to={homePath} className={`nav-link-custom ${isActive(homePath)}`}>
                  <FaHome className="me-1" /> Home
                </Nav.Link>

                {/* Role-specific navigation now lives in the left sidebar */}

                {/* User Dropdown */}
                <NavDropdown
                  title={
                    <span className="d-flex align-items-center">
                      <span className="user-avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-1"
                            style={{ width: '35px', height: '35px', fontSize: '14px', fontWeight: 600 }}>
                        {user?.full_name?.charAt(0) || 'U'}
                      </span>
                      <span className="d-none d-md-inline fw-semibold" style={{ color: '#1a202c' }}>
                        {user?.full_name?.split(' ')[0]}
                      </span>
                    </span>
                  }
                  id="user-nav-dropdown"
                  align="end"
                  className="user-dropdown"
                >
                  <NavDropdown.Item as={Link} to="/profile">
                    <FaUser className="me-2" /> Profile
                  </NavDropdown.Item>
                  <NavDropdown.Divider />
                  <NavDropdown.Item onClick={handleLogout}>
                    <FaSignOutAlt className="me-2" /> Logout
                  </NavDropdown.Item>
                </NavDropdown>
              </>
            ) : (
              <>
                <Nav.Link as={Link} to={homePath} className={`nav-link-custom ${isActive(homePath)}`}>
                  <FaHome className="me-1" /> Home
                </Nav.Link>
                <Nav.Link as={Link} to="/login" className={`btn btn-outline-primary me-2 px-4 ${isActive('/login')}`}>
                  <FaSignInAlt className="me-1" /> Login
                </Nav.Link>
                <Nav.Link as={Link} to="/register/patient" className="btn btn-primary text-white px-4">
                  <FaUserPlus className="me-1" /> Register
                </Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default AppNavbar;
