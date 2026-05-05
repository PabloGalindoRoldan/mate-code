
import './App.css'
import Landing from './components/views/Landing';
import { BrowserRouter, Routes, Route } from 'react-router';
import SysAdminView from './components/views/SysAdminView';
import LoginView from './components/views/LoginView';
import RegisterView from './components/views/RegisterView';
import ContactoView from './components/views/ContactoView';

function App() {

  return (
    <BrowserRouter basename="/mate-code">
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/admin" element={<SysAdminView />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />
        <Route path="/contact" element={<ContactoView />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
