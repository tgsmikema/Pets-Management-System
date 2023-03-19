using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SPCA_backend.Model;

namespace SPCA_backend.Data
{
    public class SPCARepo : ISPCARepo
    {
        private readonly SPCA_DBContext _dbContext;
        public SPCARepo(SPCA_DBContext dbContext)
        {
            _dbContext = dbContext;
        }

        public bool ValidLoginAdmin(string username, string password)
        {
            UserLogin userLogin = _dbContext.UserLogins.FirstOrDefault
               (e => e.UserName == username && e.Password == password && e.UserType == "admin");

            if (userLogin == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public bool ValidLoginVets(string username, string password)
        {
            UserLogin userLogin = _dbContext.UserLogins.FirstOrDefault
               (e => e.UserName == username && e.Password == password && e.UserType == "vets");

            if (userLogin == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public bool ValidLoginVolunteers(string username, string password)
        {
            UserLogin userLogin = _dbContext.UserLogins.FirstOrDefault
               (e => e.UserName == username && e.Password == password && e.UserType == "volunteers");

            if (userLogin == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
