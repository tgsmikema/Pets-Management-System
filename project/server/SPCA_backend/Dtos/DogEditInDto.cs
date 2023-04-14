using System.ComponentModel.DataAnnotations;
using System.Globalization;

namespace SPCA_backend.Dtos
{
    public class DogEditInDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Breed { get; set; }
        public int CentreId { get; set; }

    }
}
