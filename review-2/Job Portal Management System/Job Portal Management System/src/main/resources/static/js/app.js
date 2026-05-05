function filterCards(inputId, cardClass) {
  const q = document.getElementById(inputId).value.toLowerCase();
  document.querySelectorAll(`.${cardClass}`).forEach(card => {
    const text = card.innerText.toLowerCase();
    card.style.display = text.includes(q) ? "block" : "none";
  });
}

function setSkillChip(skill) {
  const input = document.getElementById("skillFilter");
  if (!input) return;
  input.value = skill;
  applyJobFilters();
}

function applyJobFilters() {
  const q = (document.getElementById("searchInput")?.value || "").toLowerCase();
  const location = (document.getElementById("locationFilter")?.value || "").toLowerCase();
  const exp = parseInt(document.getElementById("expFilter")?.value || "", 10);
  const skill = (document.getElementById("skillFilter")?.value || "").toLowerCase();

  document.querySelectorAll(".job-card").forEach(card => {
    const text = card.innerText.toLowerCase();
    const cardLocation = (card.dataset.location || "").toLowerCase();
    const cardExp = parseInt(card.dataset.exp || "0", 10);
    const cardSkills = (card.dataset.skills || "").toLowerCase();

    const matchesText = !q || text.includes(q);
    const matchesLocation = !location || cardLocation.includes(location);
    const matchesExp = Number.isNaN(exp) || cardExp <= exp;
    const matchesSkill = !skill || cardSkills.includes(skill);

    card.style.display = matchesText && matchesLocation && matchesExp && matchesSkill ? "block" : "none";
  });
}
