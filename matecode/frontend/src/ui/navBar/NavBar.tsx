import { useNavigate } from "react-router";
import { useAuth } from "../../context/AuthContext";
import './NavBar.css';

export default function NavBar() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  // Helper to extract initials (e.g., "Pablo Galindo" -> "PG")
  const getInitials = () => {
    if (!user || !user.nombre || !user.apellido) return "??";
    const firstInitial = user.nombre.charAt(0).toUpperCase();
    const secondInitial = user.apellido.charAt(0).toUpperCase();
    return `${firstInitial}${secondInitial}`;
  };

  // Helper to send the logged-in user back to their specific dashboard role
  const handleDashboardRedirect = () => {
    if (!user) return;

    switch (user.rol) {
      case 'ADMINISTRADOR_SISTEMA':
        navigate('/admin');
        break;
      case 'ADMINISTRADOR_PARQUE':
        navigate('/parque');
        break;
      case 'REPRESENTANTE_EMPRESA':
        navigate('/empresa-radicada');
        break;
      default:
        navigate('/');
        break;
    }
  };

  const handleLogoutClick = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <h1 className="logo">
        <a onClick={() => navigate('/')}>RN</a>
      </h1>

      <ul className="buttonList">
        {/* CONDITION 1: USER IS NOT LOGGED IN */}
        {!user ? (
          <>
            <li>
              <button className="navButtonContacto" onClick={() => navigate('/contacto')}>
                Contacto
              </button>
            </li>
            <li>
              <button className="navButtonIngresar" onClick={() => navigate('/login')}>
                Ingresar
              </button>
            </li>
            <li>
              <button className="navButtonRegistrarse" onClick={() => navigate('/register')}>
                Registrarse
              </button>
            </li>
          </>
        ) : (
          /* CONDITION 2: USER IS LOGGED IN */
          <li className="navUserSection">
            <div
              className="userAvatar"
              onClick={handleDashboardRedirect}
              title="Ir al panel de control"
            >
              {getInitials()}
            </div>

            {/* Logout Action */}
            <button className="navButtonRegistrarse" onClick={handleLogoutClick}>
              Cerrar Sesión
            </button>
          </li>
        )}
      </ul>
    </nav>
  );
}