// Landing.tsx
import LandingBody from './LandingBody';
import PublicacionesView from '../publicaciones/PublicacionesView';
import Footer from '../../ui/footer/Footer';
import NavBar from '../../ui/navBar/NavBar';
import './Landing.css';

export default function Landing() {
  return (
    <div className="landingPageContainer">
      <div className="heroSectionWrapper">
        <NavBar variant="transparent" />
        <LandingBody />
      </div>
      <main className="landingMainContent">
        <PublicacionesView />
      </main>
      <Footer />
    </div>
  );
}