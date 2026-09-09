import { HiOutlineUser } from "react-icons/hi";

interface AuthHeaderProps {
    title: string;
}

const AuthHeader = ({ title }: AuthHeaderProps) => {
    return (
        <div className="flex items-center gap-3 mb-6">
            <div className="bg-blue-900/10 p-2 rounded-full text-blue-900">
                <HiOutlineUser className="w-6 h-6" />
            </div>
            <h1 className="text-blue-900 font-medium text-xl">{title}</h1>
        </div>
    );
};

export default AuthHeader;