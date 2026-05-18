import './ImageContainer.css'
import data from '../../../tmp/carrouselLanding.json'
import { useState } from 'react';

export default function ImageContainer() {

    const [imagenes] = useState(data);

    function randomizeImage() {
        const randomIndex = Math.floor(Math.random() * imagenes.carrousel.length);
        return imagenes.carrousel[randomIndex].image;
    }

    return (
        <div className="imageContainer">
            <img src={randomizeImage()} alt="Imagen de fondo" className="landingImage" />
        </div>
    )
}