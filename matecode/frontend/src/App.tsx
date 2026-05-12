
import './App.css'
import Landing from './components/views/Landing';
import { BrowserRouter, Routes, Route } from 'react-router';
import SysAdminView from './components/views/SysAdminView';

function App() {

  return (
    <BrowserRouter basename="/mate-code">
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/admin" element={<SysAdminView />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
