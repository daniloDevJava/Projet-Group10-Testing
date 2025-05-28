import { useState } from 'react'
import { BrowserRouter as Router, Route, Routes  } from 'react-router-dom'
import Sign from './pages/sign'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Router>
       <div className="App">
        <Routes>
          <Route path="/sign" element={<Sign/>} />
        </Routes>
      </div>
    </Router>
    </>
  )
}

export default App
