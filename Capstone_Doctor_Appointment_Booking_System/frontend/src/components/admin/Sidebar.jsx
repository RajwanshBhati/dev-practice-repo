import { Link, useLocation } from 'react-router-dom';

/**
 * Reusable left sidebar used on Admin / Doctor / Patient dashboard pages.
 * @param {Array} items - [{ to, label, icon, badge }]
 */
const Sidebar = ({ items = [], title }) => {
  const location = useLocation();
  const isActive = (to) => location.pathname === to;

  return (
    <div className="dashboard-sidebar">
      {title && (
        <p className="dashboard-sidebar-title text-uppercase text-muted fw-bold px-2 mb-3"
           style={{ fontSize: '0.72rem', letterSpacing: '0.06em' }}>
          {title}
        </p>
      )}
      <div className="dashboard-sidebar-nav">
        {items.map((item) => (
          <Link key={item.to} to={item.to}
            className="d-flex align-items-center justify-content-between text-decoration-none px-3 py-2"
            style={{
              borderRadius: '10px', fontWeight: 600, fontSize: '0.92rem',
              color: isActive(item.to) ? '#ffffff' : '#4a5568',
              background: isActive(item.to) ? 'linear-gradient(135deg, #4a90d9, #357abd)' : 'transparent',
              transition: 'background 0.15s ease',
            }}>
            <span className="d-flex align-items-center">
              <span className="me-2" style={{ fontSize: '16px' }}>{item.icon}</span>
              {item.label}
            </span>
            {!!item.badge && (
              <span
                style={{
                  background: isActive(item.to) ? 'rgba(255,255,255,0.25)' : '#f59e0b',
                  color: '#fff',
                  borderRadius: '999px',
                  fontSize: '0.72rem',
                  fontWeight: 700,
                  padding: '2px 8px',
                  minWidth: '20px',
                  textAlign: 'center',
                }}
              >
                {item.badge}
              </span>
            )}
          </Link>
        ))}
      </div>
    </div>
  );
};

export default Sidebar;
