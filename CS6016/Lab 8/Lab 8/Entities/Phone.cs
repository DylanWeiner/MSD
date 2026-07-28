using System;
using System.Collections.Generic;

namespace Lab_8.Entities;

public partial class Phone
{
    public int Cardnum { get; set; }

    public string Phone1 { get; set; } = null!;

    public virtual Patron CardnumNavigation { get; set; } = null!;
}
