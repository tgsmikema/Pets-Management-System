using System.ComponentModel.DataAnnotations;
using System.Globalization;

namespace SPCA_backend.Dtos
{
    public class DogOutDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Breed { get; set; }
        public int CentreId { get; set; }
        public bool isFlag { get; set; }
        public bool isAlert { get; set; }
        public string LastCheckInTimeStamp { get; set; }
        public double LastCheckInWeight { get; set; }

    }
}
