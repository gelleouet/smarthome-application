<%@ page import="smarthome.application.Defi" %>


<div class="form-group required">
	<label for="libelle">Libellé</label>
	<g:textField name="libelle" required="true" value="${defi?.libelle}" class="form-control"/>
</div>

<div class="form-group required">
	<label for="organisation">Organisé par</label>
	<g:textField name="organisation" value="${defi?.organisation}" class="form-control"/>
</div>
<small class="text-muted">A renseigner si vous avez créé le défi pour le coompte d'une organisation. Ce nom apparaîtra sur la fiche catalogue</small>

<div class="form-group required">
	<label for="referenceDebut">Début Référence</label>
	<g:field type="date" name="referenceDebut" class="form-control aui-date-picker" value="${ app.formatPicker(date: defi?.referenceDebut) }" required="true"/>
</div>

<div class="form-group required">
	<label for="referenceFin">Fin Référence</label>
	<g:field type="date" name="referenceFin" class="form-control aui-date-picker" value="${ app.formatPicker(date: defi?.referenceFin) }" required="true"/>
</div>

<div class="form-group required">
	<label for="actionDebut">Début Action</label>
	<g:field type="date" name="actionDebut" class="form-control aui-date-picker" value="${ app.formatPicker(date: defi?.actionDebut) }" required="true"/>
</div>

<div class="form-group required">
	<label for="actionFin">Fin Action</label>
	<g:field type="date" name="actionFin" class="form-control aui-date-picker" value="${ app.formatPicker(date: defi?.actionFin) }" required="true"/>
</div>

<label class="custom-control custom-checkbox">
	<g:checkBox name="publique" value="${defi?.publique}" class="custom-control-input"/>
	<span class="custom-control-label">Publique ?</span>
</label>
<small class="text-muted">Si activé, ce défi sera accessible à tous les utilisateurs connectés de l'application</small>

<label class="custom-control custom-checkbox">
	<g:checkBox name="actif" value="${defi?.actif}" class="custom-control-input"/>
	<span class="custom-control-label">Actif ?</span>
</label>
<small class="text-muted">Si désactivé, les inscriptions ne sont plus autorisées</small>

