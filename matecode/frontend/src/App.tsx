import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router';
import Landing from './features/landing/Landing';
import SysAdminView from './features/sysadmin-dashboard/SysAdminView';
import LoginView from './features/auth/LoginView';
import RegisterView from './features/auth/RegisterView';
import ContactoView from './features/landing/ContactoView';
import EmpresaNoRadicadaView from './features/empresa-dashboard/EmpresaNoRadicadaView';
import EmpresaRadicadaView from './features/empresa-dashboard/EmpresaRadicadaView';
import AdministradorParqueView from './features/parque-dashboard/AdministradorParqueView';
import ProtectedRoute from './routes/ProtectedRoute';
import { Toaster } from 'sonner';

function App() {
  return (
    <BrowserRouter basename="/mate-code">
      <Toaster position="top-right" richColors />
      <Routes>
        {/* Public Routes accessible by anyone */}
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />
        <Route path="/contacto" element={<ContactoView />} />

        {/* Administrador del Parque Dashboard Only */}
        <Route
          path="/parque"
          element={
            <ProtectedRoute allowedRoles={['ADMINISTRADOR_PARQUE']}>
              <AdministradorParqueView />
            </ProtectedRoute>
          }
        />

        {/* Sistema Admin Dashboard Only */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRoles={['ADMINISTRADOR_SISTEMA']}>
              <SysAdminView />
            </ProtectedRoute>
          }
        />

        {/* Representante Empresa Exclusives */}
        <Route
          path="/nueva-empresa"
          element={
            <ProtectedRoute allowedRoles={['REPRESENTANTE_EMPRESA']}>
              <EmpresaNoRadicadaView />
            </ProtectedRoute>
          }
        />
        <Route
          path="/empresa-radicada"
          element={
            <ProtectedRoute allowedRoles={['REPRESENTANTE_EMPRESA']}>
              <EmpresaRadicadaView />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;