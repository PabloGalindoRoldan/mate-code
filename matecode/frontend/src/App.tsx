
import './App.css'
import Landing from './components/views/Landing';
import { BrowserRouter, Routes, Route } from 'react-router';
import SysAdminView from './components/views/SysAdminView';
import LoginView from './components/views/LoginView';
import RegisterView from './components/views/RegisterView';
import ContactoView from './components/views/ContactoView';
import EmpresaNoRadicadaView from './components/views/EmpresaNoRadicadaView';
import EmpresaRadicadaView from './components/views/EmpresaRadicadaView';
import AdministradorParqueView from './components/views/AdministradorParqueView';

function App() {

  return (

    <BrowserRouter basename="/mate-code">
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/parque" element={<AdministradorParqueView />} />
        <Route path="/admin" element={<SysAdminView />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />
        <Route path="/contacto" element={<ContactoView />} />
        <Route path="/nueva-empresa" element={<EmpresaNoRadicadaView />} />
        <Route path="/empresa-radicada" element={<EmpresaRadicadaView />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
