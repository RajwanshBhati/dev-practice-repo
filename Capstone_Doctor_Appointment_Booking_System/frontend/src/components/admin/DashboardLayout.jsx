import { useState, useEffect, useCallback, createContext, useContext } from 'react';
import Sidebar from './Sidebar';
import { getPendingProfileUpdates } from '../../api/admin';
import {
  FaTachometerAlt, FaUserMd, FaClipboardList, FaUserShield,
  FaSearch, FaCalendarCheck, FaMoneyBillWave, FaUserCircle, FaClock, FaEdit,
} from 'react-icons/fa';

/**
 * Lets pages nested inside the Admin dashboard
 */
const DashboardRefreshContext = createContext(() => {});
export const useDashboardRefresh = () => useContext(DashboardRefreshContext);

const buildSidebarConfig = (pendingUpdateCount) => ({
  ADMIN: {
    title: 'Admin Panel',
    items: [
      { to: '/admin/dashboard', label: 'Dashboard', icon: <FaTachometerAlt /> },
      { to: '/admin/doctors', label: 'Manage Doctors', icon: <FaUserMd /> },
      {
        to: '/admin/profile-updates',
        label: 'Profile Update Requests',
        icon: <FaEdit />,
        badge: pendingUpdateCount > 0 ? pendingUpdateCount : null,
      },
      { to: '/admin/audit-logs', label: 'View Audit Logs', icon: <FaClipboardList /> },
      { to: '/admin/profile', label: 'My Profile', icon: <FaUserShield /> },
    ],
  },
  DOCTOR: {
    title: 'Doctor Panel',
    items: [
      { to: '/doctor/dashboard', label: 'Dashboard', icon: <FaTachometerAlt /> },
      { to: '/doctor/availability', label: 'Availability', icon: <FaClock /> },
      { to: '/doctor/appointments', label: 'Appointments', icon: <FaCalendarCheck /> },
      { to: '/doctor/profile', label: 'My Profile', icon: <FaUserCircle /> },
    ],
  },
  PATIENT: {
    title: 'Patient Panel',
    items: [
      { to: '/home', label: 'Dashboard', icon: <FaTachometerAlt /> },
      { to: '/search-doctors', label: 'Search Doctor', icon: <FaSearch /> },
      { to: '/my-appointments', label: 'My Appointments', icon: <FaCalendarCheck /> },
      { to: '/payment-history', label: 'Payments', icon: <FaMoneyBillWave /> },
      { to: '/patient/profile', label: 'My Profile', icon: <FaUserCircle /> },
    ],
  },
});

/**
 * Wraps a dashboard page with the correct role-based sidebar.
 * Usage: <DashboardLayout role="ADMIN"><AdminDashboard /></DashboardLayout>
 */
const DashboardLayout = ({ role, children }) => {
  const [pendingUpdateCount, setPendingUpdateCount] = useState(0);

  const loadPendingCount = useCallback(async () => {
    if (role !== 'ADMIN') return;
    try {
      const data = await getPendingProfileUpdates();
      setPendingUpdateCount((data.doctors || []).length);
    } catch (error) {
      // Silently ignore — badge just won't show a count.
    }
  }, [role]);

  useEffect(() => {
    if (role !== 'ADMIN') return;

    loadPendingCount();
    // Re-check periodically so the badge stays fresh while the admin is browsing.
    const interval = setInterval(loadPendingCount, 30000);
    return () => {
      clearInterval(interval);
    };
  }, [role, loadPendingCount]);

  const config = buildSidebarConfig(pendingUpdateCount)[role];

  if (!config) return children;

  return (
    <DashboardRefreshContext.Provider value={loadPendingCount}>
      <div className="dashboard-layout" style={{ margin: '0 -1.5rem' }}>
        <Sidebar title={config.title} items={config.items} />
        <div className="dashboard-sidebar-content">{children}</div>
      </div>
    </DashboardRefreshContext.Provider>
  );
};

export default DashboardLayout;
