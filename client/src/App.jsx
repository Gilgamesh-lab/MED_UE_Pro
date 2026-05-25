import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar'
import Home from './pages/Home';
import About from './pages/About';

function App() {

  return (
    <BrowserRouter>
      <div className='flex flex-col h-screen'>
        <Navbar />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/a-propos" element={<About />} />
        </Routes>
      </div>


    </BrowserRouter>

  )
}

export default App
