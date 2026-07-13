import { BrowserRouter as Router, Routes, Route,Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider,useAuth } from './context/AuthContext';
import PrivateRoute from './components/common/PrivateRoute';
import Navbar from './components/common/Navbar';
import ForgotPassword from './components/auth/ForgotPassword';
import DoctorSearch from './components/patient/DoctorSearch';
import DoctorDetails from './components/patient/DoctorDetails';
import BookAppointment from './components/patient/BookAppointment';
import DashboardLayout from './components/admin/DashboardLayout';

// Auth Components
import Login from './components/auth/Login';
import RegisterPatient from './components/auth/RegisterPatient';
import RegisterDoctor from './components/auth/RegisterDoctor';
import ResetPassword from './components/auth/ResetPassword';

// Patient Components
import Home from './components/patient/Home';
import MyAppointments from './components/patient/MyAppointments';
import Welcome from './components/patient/Welcome';
import PatientProfile from './components/patient/PatientProfile';

//Doctor Components
import DoctorDashboard from './components/doctor/DoctorDashboard';
import DoctorProfile from './components/doctor/DoctorProfile';
import DoctorAppointments from './components/doctor/DoctorAppointments';
import ManageAvailability from './components/doctor/ManageAvailability';
import ProfileUpdateRequests from './components/admin/ProfileUpdateRequests';
//admin Components
import AdminDashboard from './components/admin/AdminDashboard';
import ManageDoctors from './components/admin/ManageDoctors';
import AuditLogs from './components/admin/AuditLogs';
import AdminProfile from './components/admin/AdminProfile';

//Payment Components
import PaymentPage from './components/patient/PaymentPage';
import PaymentHistory from './components/patient/PaymentHistory';

import './styles/global.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function ProfileRedirect() {
  const { user } = useAuth();
  if (user?.role === 'DOCTOR') return <Navigate to="/doctor/profile" replace />;
  if (user?.role === 'ADMIN') return <Navigate to="/admin/profile" replace />;
  if (user?.role === 'PATIENT') return <Navigate to="/patient/profile" replace />;
  return <Navigate to="/" replace />;
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="App">
          <Navbar />
          <main className="container-fluid px-4 py-3">
            <Routes>
              {/* Public Routes */}
              <Route path="/login" element={<Login />} />
              <Route path="/register/patient" element={<RegisterPatient />} />
              <Route path="/register/doctor" element={<RegisterDoctor />} />
              <Route path="/" element={
                 <Welcome />
                } />
              {/* Protected Routes */}
              <Route path="/home" element={
                <PrivateRoute allowedRoles={['PATIENT', 'DOCTOR', 'ADMIN']}>
                  <Home />
                </PrivateRoute>
              } />
              <Route path="/search-doctors" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <DashboardLayout role="PATIENT"><DoctorSearch /></DashboardLayout>
                </PrivateRoute>
              } />

               <Route path="/doctors/:doctorId" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <DashboardLayout role="PATIENT"><DoctorDetails /></DashboardLayout>
                </PrivateRoute>
              } />

               <Route path="/patient/profile" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <DashboardLayout role="PATIENT"><PatientProfile /></DashboardLayout>
                </PrivateRoute>
              } />

              <Route path="/reset-password" element={<ResetPassword />} />
              <Route path="/book-appointment/:doctorId" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <DashboardLayout role="PATIENT"><BookAppointment /></DashboardLayout>
                </PrivateRoute>
              } />

              <Route path="/my-appointments" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                 <DashboardLayout role="PATIENT"><MyAppointments /></DashboardLayout>
                </PrivateRoute>
                 } />

                 {/* Doctor Routes */}
              <Route path="/doctor/dashboard" element={
               <PrivateRoute allowedRoles={['DOCTOR']}>
               <DashboardLayout role="DOCTOR"><DoctorDashboard /></DashboardLayout>
              </PrivateRoute>
               } />
               <Route path="/doctor/profile" element={
              <PrivateRoute allowedRoles={['DOCTOR']}>
               <DashboardLayout role="DOCTOR"><DoctorProfile /></DashboardLayout>
               </PrivateRoute>
                } />
              <Route path="/doctor/appointments" element={
               <PrivateRoute allowedRoles={['DOCTOR']}>
              <DashboardLayout role="DOCTOR"><DoctorAppointments /></DashboardLayout>
               </PrivateRoute>
                } />


                <Route path="/doctor/availability" element={
                <PrivateRoute allowedRoles={['DOCTOR']}>
                <DashboardLayout role="DOCTOR"><ManageAvailability /></DashboardLayout>
                 </PrivateRoute>
                } />

                {/* Admin Routes */}
                <Route path="/admin/dashboard" element={
                 <PrivateRoute allowedRoles={['ADMIN']}>
                <DashboardLayout role="ADMIN"><AdminDashboard /></DashboardLayout>
                </PrivateRoute>
                } />

                <Route path="/admin/profile-updates" element={
                <PrivateRoute allowedRoles={['ADMIN']}>
                <DashboardLayout role="ADMIN"><ProfileUpdateRequests /></DashboardLayout>
               </PrivateRoute>
                } />

                <Route path="/admin/doctors" element={
                <PrivateRoute allowedRoles={['ADMIN']}>
               <DashboardLayout role="ADMIN"><ManageDoctors /></DashboardLayout>
               </PrivateRoute>
                } />


                <Route path="/admin/audit-logs" element={
                <PrivateRoute allowedRoles={['ADMIN']}>
                <DashboardLayout role="ADMIN"><AuditLogs /></DashboardLayout>
               </PrivateRoute>
                } />




                <Route path="/payment/:appointmentId" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                <PaymentPage />
                </PrivateRoute>
                 } />
                <Route path="/payment-history" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                 <DashboardLayout role="PATIENT"><PaymentHistory /></DashboardLayout>
                 </PrivateRoute>
                 } />


                 <Route path="/admin/profile" element={
                  <PrivateRoute allowedRoles={['ADMIN']}>
                 <DashboardLayout role="ADMIN"><AdminProfile /></DashboardLayout>
                 </PrivateRoute>
                } />


                 <Route path="/profile" element={
                 <PrivateRoute allowedRoles={['PATIENT', 'DOCTOR', 'ADMIN']}>
                 <ProfileRedirect />
                </PrivateRoute>
                } />




              <Route path="/forgot-password" element={<ForgotPassword />} />
            </Routes>
          </main>
          <Toaster
            position="top-right"
            toastOptions={{
              duration: 4000,
              style: {
                background: '#fff',
                color: '#333',
              },
              success: {
                duration: 3000,
                iconTheme: {
                  primary: '#28a745',
                  secondary: '#fff',
                },
              },
              error: {
                duration: 4000,
                iconTheme: {
                  primary: '#dc3545',
                  secondary: '#fff',
                },
              },
            }}
          />
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
