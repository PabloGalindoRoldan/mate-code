
import './App.css'
import Landing from './components/views/Landing';
import { BrowserRouter, Routes, Route } from 'react-router';
import SysAdminView from './components/views/SysAdminView';
import LoginView from './components/views/LoginView';

function App() {

  return (
    <BrowserRouter basename="/mate-code">
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/admin" element={<SysAdminView />} />
        <Route path="/login" element={<LoginView />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
