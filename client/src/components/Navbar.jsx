function Navbar() {
    return (
        <div className="flex flex-row items-center gap">
            <div className="flex">
                <h1 className="text-3xl">metro-efrei-dodo</h1>
            </div>
            <div className="flex w-[70%] justify-left">
                <a>à propos</a>
                <a>github</a>
            </div>
        </div>
    )
}

export default Navbar;