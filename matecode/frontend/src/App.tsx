import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router';
import Landing from './components/views/Landing';
import SysAdminView from './components/views/SysAdminView';
import LoginView from './components/views/LoginView';
import RegisterView from './components/views/RegisterView';
import ContactoView from './components/views/ContactoView';
import EmpresaNoRadicadaView from './components/views/EmpresaNoRadicadaView';
import EmpresaRadicadaView from './components/views/EmpresaRadicadaView';
import AdministradorParqueView from './components/views/AdministradorParqueView';
import ProtectedRoute from './components/shared/ProtectedRoute'; // <-- Import the wrapper

function App() {
  return (
    <BrowserRouter basename="/mate-code">
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