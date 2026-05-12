import './ImageContainer.css'

export default function ImageContainer() {

    function randomizeImage() {
        const images = [
            "https://images.pexels.com/photos/19544248/pexels-photo-19544248.jpeg",
            "https://images.pexels.com/photos/29543207/pexels-photo-29543207.jpeg",
            "https://images.pexels.com/photos/18920790/pexels-photo-18920790.jpeg"
        ];
        const randomIndex = Math.floor(Math.random() * images.length);
        return images[randomIndex];
    }

    return (
        <div className="imageContainer">
            <img src={randomizeImage()} alt="Imagen de fondo" className="landingImage" />
        </div>
    )
}