import React from "react";
import "./checkbox.css"; 

export const Checkbox = ({ checked, onChange }) => (
  <input
    type="checkbox"
    checked={checked}
    onChange={onChange}
    className="custom-checkbox"
  />
);
