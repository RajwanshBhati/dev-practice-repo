import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/common/PrivateRoute';
import Navbar from './components/common/Navbar';
import ForgotPassword from './components/auth/ForgotPassword';
import DoctorSearch from './components/patient/DoctorSearch';
import DoctorDetails from './components/patient/DoctorDetails';
import BookAppointment from './components/patient/BookAppointment';

// Auth Components
import Login from './components/auth/Login';
import RegisterPatient from './components/auth/RegisterPatient';
import RegisterDoctor from './components/auth/RegisterDoctor';

// Patient Components
import Home from './components/patient/Home';

import './styles/global.css';
import 'bootstrap/dist/css/bootstrap.min.css';

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

              {/* Protected Routes */}
              <Route path="/" element={
                <PrivateRoute allowedRoles={['PATIENT', 'DOCTOR', 'ADMIN']}>
                  <Home />
                </PrivateRoute>
              } />
              <Route path="/search-doctors" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <DoctorSearch />
                </PrivateRoute>
              } />

               <Route path="/doctors/:doctorId" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <DoctorDetails />
                </PrivateRoute>
              } />
              <Route path="/book-appointment/:doctorId" element={
                <PrivateRoute allowedRoles={['PATIENT']}>
                  <BookAppointment />
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
