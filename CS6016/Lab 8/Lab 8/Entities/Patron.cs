using System;
using System.Collections.Generic;

namespace Lab_8.Entities;

public partial class Patron
{
    public int Cardnum { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<Checkedout> Checkedouts { get; set; } = new List<Checkedout>();

    public virtual ICollection<Phone> Phones { get; set; } = new List<Phone>();
}
