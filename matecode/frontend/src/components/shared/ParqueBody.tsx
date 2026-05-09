import MapPanel from "./MapPanel";
import "./ParqueBody.css"


export default function ParqueBody(params: { empresaInfo: any, isMenuOpen: boolean }) {

    const { isMenuOpen } = params;

    return (
        <>
            <div className={`ParqueBodyContainer ${isMenuOpen ? "shrunk" : "full"}`}>
                <MapPanel />
            </div>
        </>
    )
}