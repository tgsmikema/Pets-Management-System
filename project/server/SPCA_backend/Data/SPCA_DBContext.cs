using Microsoft.EntityFrameworkCore;
using SPCA_backend.Model;

namespace SPCA_backend.Data
{
    public class SPCA_DBContext : DbContext
    {
        public SPCA_DBContext(DbContextOptions<SPCA_DBContext> options) : base(options) { }
        public DbSet<Customer> Customers { get; set; }
    }
}
