import { useState } from "react";

function Sidebar() {
    const [departure, setDeparture] = useState("");
    const [arrival, setArrival] = useState("")
    const [route, setRoute] = useState("")

    const handleSearch = async () => {
        const response = await fetch(`http://localhost:8080/chemin?departure=${departure}&arrival=${arrival}`);
        const routeText = await response.text();
        setRoute(routeText);
    }

    return (
        <div className="flex flex-col m-5">
            <div className="mb-4">
                <h1 className="text-2xl text-[#123ABF] font-extrabold">itinéraire</h1>
            </div>
            <div className="flex flex-col w-full mb-4">
                <p className="text-lg text-[#123ABF] mb-2">départ</p>
                <input type="text" value={departure} onChange={(e) => setDeparture(e.target.value)} placeholder="ex: Maison Blanche" className="border border-[#c0caf8] h-10 w-full p-2 mb-3 rounded-[10px] text-lg text-[#123ABF] focus:outline-0" />

                <p className="text-lg text-[#123ABF] mb-2">arrivée</p>
                <input type="text" value={arrival} onChange={(e) => setArrival(e.target.value)} placeholder="ex: Villejuif Louis Aragon" className="border border-[#c0caf8] h-10 w-full p-2 mb-3 rounded-[10px] text-lg text-[#123ABF] focus:outline-0" />

                <button onClick={handleSearch} className="border border-[#c0caf8] hover:border-[#c0caf8] focus:border-[#123ABF] bg-white hover:bg-[#c0caf8]/20 focus:bg-[#123ABF] text-lg text-[#123ABF] focus:text-white rounded-[10px] p-[0.25rem]">rechercher</button>
            </div>
            <div>
                {route && <p className="text-[#123ABF]">{route}</p>}
            </div>
        </div>
    )
}

export default Sidebar;