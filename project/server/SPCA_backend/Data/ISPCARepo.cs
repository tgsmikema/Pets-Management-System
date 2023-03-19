using SPCA_backend.Model;

namespace SPCA_backend.Data
{
    public interface ISPCARepo
    {
        public bool ValidLoginAdmin(string username, string password);
        public bool ValidLoginVets(string username, string password);
        public bool ValidLoginVolunteers(string username, string password);
    }
}
