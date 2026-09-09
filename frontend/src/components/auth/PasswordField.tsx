import { useState } from "react";
import { HiOutlineEye, HiOutlineEyeOff, HiOutlineLockClosed } from "react-icons/hi";
import InputField from "./InputField";

interface PasswordFieldProps {
  id: string;
  label: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
  error?: string;
  required?: boolean;
}

const PasswordField = ({
                         id,
                         label,
                         value,
                         onChange,
                         placeholder = "••••••••",
                         error,
                         required = false,
                       }: PasswordFieldProps) => {
  const [show, setShow] = useState(false);

  return (
      <InputField
          id={id}
          label={label}
          type={show ? "text" : "password"}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          icon={<HiOutlineLockClosed className="w-5 h-5 opacity-50" />}
          error={error}
          required={required}
          rightElement={
            <button
                type="button"
                onClick={() => setShow(!show)}
                className="text-gray-400 hover:text-gray-600 focus:outline-none"
            >
              {show ? <HiOutlineEyeOff className="w-5 h-5" /> : <HiOutlineEye className="w-5 h-5" />}
            </button>
          }
      />
  );
};

export default PasswordField;