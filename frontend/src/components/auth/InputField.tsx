import React, { type ReactNode } from "react";

interface InputFieldProps {
    id: string;
    label: string;
    type: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder?: string;
    icon?: ReactNode;
    error?: string;
    required?: boolean;
    rightElement?: ReactNode;
    onInvalid?: (event: React.InvalidEvent<HTMLInputElement>) => void; // ← исправлено
}

const InputField = ({
                        id,
                        label,
                        type,
                        value,
                        onChange,
                        placeholder,
                        icon,
                        error,
                        required = false,
                        rightElement,
                        onInvalid,
                    }: InputFieldProps) => {
    return (
        <div>
            <label htmlFor={id} className="block text-sm font-medium text-gray-700 mb-1">
                {label}
            </label>
            <div className="relative">
                {icon && (
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        {icon}
                    </div>
                )}
                <input
                    id={id}
                    type={type}
                    value={value}
                    onChange={onChange}
                    required={required}
                    onInvalid={onInvalid}
                    className={`block w-full ${icon ? 'pl-10' : 'pl-3'} pr-3 py-2 border ${
                        error ? 'border-red-500' : 'border-gray-300'
                    } rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 text-sm`}
                    placeholder={placeholder}
                />
                {rightElement && (
                    <div className="absolute inset-y-0 right-0 pr-3 flex items-center">
                        {rightElement}
                    </div>
                )}
            </div>
            {error && <p className="text-red-500 text-sm mt-1">{error}</p>}
        </div>
    );
};

export default InputField;