function Sidebar() {
    return (
        <div className="flex flex-col m-5">
            <div className="mb-4">
                <h1 className="text-2xl text-[#123ABF] font-extrabold">itinéraire</h1>
            </div>
            <div className="flex flex-col w-full">
                <p className="text-lg text-[#123ABF] mb-2">départ</p>
                <input type="" placeholder="ex: Maison Blanche" className="border border-[#c0caf8] h-10 w-full p-2 mb-3 rounded-[10px] text-lg text-[#123ABF] focus:outline-0" />

                <p className="text-lg text-[#123ABF] mb-2">arrivée</p>
                <input type="" placeholder="ex: Villejuif Louis Aragon" className="border border-[#c0caf8] h-10 w-full p-2 mb-3 rounded-[10px] text-lg text-[#123ABF] focus:outline-0" />

                <button className="border border-[#c0caf8] hover:border-[#c0caf8] focus:border-[#123ABF] bg-white hover:bg-[#c0caf8]/20 focus:bg-[#123ABF] text-lg text-[#123ABF] focus:text-white rounded-[10px] p-[0.25rem]">rechercher</button>
            </div>
        </div>
    )
}

export default Sidebar;