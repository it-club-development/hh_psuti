import { FcGoogle } from "react-icons/fc";
import { FaVk } from "react-icons/fa";

const SocialButtons = () => {
    return (
        <div className="mt-6">
            <div className="relative">
                <div className="absolute inset-0 flex items-center">
                    <div className="w-full border-t border-gray-200"></div>
                </div>
                <div className="relative flex justify-center text-sm">
                    <span className="px-2 bg-white text-gray-500">или</span>
                </div>
            </div>

            <div className="mt-4 grid grid-cols-2 gap-3">
                <button className="flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                    <FcGoogle className="w-5 h-5 mr-2" />
                    Google
                </button>
                <button className="flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors">
                    <FaVk className="w-5 h-5 mr-2" />
                    VK
                </button>
            </div>
        </div>
    );
};

export default SocialButtons;