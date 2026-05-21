// NavBar.tsx
import { useNavigate } from "react-router";
import { useAuth } from "../../context/AuthContext";
import './NavBar.css';

interface NavBarProps {
  variant?: "transparent" | "solid";
}

export default function NavBar({ variant = "solid" }: NavBarProps) {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const getInitials = () => {
    if (!user || !user.nombre || !user.apellido) return "??";
    return `${user.nombre.charAt(0).toUpperCase()}${user.apellido.charAt(0).toUpperCase()}`;
  };

  const handleDashboardRedirect = () => {
    if (!user) return;
    switch (user.rol) {
      case 'ADMINISTRADOR_SISTEMA': navigate('/admin'); break;
      case 'ADMINISTRADOR_PARQUE': navigate('/parque'); break;
      case 'REPRESENTANTE_EMPRESA': navigate('/empresa-radicada'); break;
      default: navigate('/'); break;
    }
  };

  return (
    /* Si la variante es transparent, se agregan ambas clases */
    <nav className={`navbar ${variant === 'transparent' ? 'navbar-transparent' : ''}`}>
      <h1 className="logo">
        <a onClick={() => navigate('/')}>RN</a>
      </h1>

      <ul className="buttonList">
        {!user ? (
          <>
            <li><button className="navButtonContacto" onClick={() => navigate('/contacto')}>Contacto</button></li>
            <li><button className="navButtonIngresar" onClick={() => navigate('/login')}>Ingresar</button></li>
            <li><button className="navButtonRegistrarse" onClick={() => navigate('/register')}>Registrarse</button></li>
          </>
        ) : (
          <li className="navUserSection">
            <div className="userAvatar" onClick={handleDashboardRedirect} title="Ir al panel de control">
              {getInitials()}
            </div>
            <button className="navButtonCerrarSesion" onClick={() => { logout(); navigate('/'); }}>
              Cerrar Sesión
            </button>
          </li>
        )}
      </ul>
    </nav>
  );
}