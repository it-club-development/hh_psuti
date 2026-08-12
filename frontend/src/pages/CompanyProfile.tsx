import BackHeader from "../components/BackHeader";
import CompanyCard from "../components/CompanyCard";
import ActionButtons from "../components/ActionButtons";
import CompanyDetails from "../components/CompanyDetails";

const company = {
  name: 'ИП Иванов',
  email: 'ivan@mail.ru',
  phone: '+7 987 45 56 12 2',
};

const CompanyProfile = () => {
  return (
    <div className="min-h-screen bg-white p-4 md:p-8 font-sans">
      <div className="max-w-6xl mx-auto">
        <BackHeader />
        <main className="mt-8 grid grid-cols-1 lg:grid-cols-2 gap-8 items-start">
          <div className="space-y-6">
            <CompanyCard company={company} />
            <ActionButtons />
          </div>
          <div className="h-full">
            <CompanyDetails />
          </div>
        </main>
      </div>
    </div>
  );
};

export default CompanyProfile;
